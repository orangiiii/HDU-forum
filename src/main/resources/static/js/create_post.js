// 发帖页面逻辑

let draftId = null;
let autoSaveTimer = null;
let lastGradYear = null; // 记录上次选择的届数

// 加载分类列表
async function loadCategories() {
    const result = await fetchGet(API.CATEGORY_LIST);
    if (result.code === 200) {
        const select = document.getElementById('categoryId');
        result.data.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat.id;
            option.textContent = cat.name;
            select.appendChild(option);
        });
    }
}

// 加载草稿（如果有）
async function loadDraft() {
    const result = await fetchGet(API.POST_DRAFT_LATEST);
    if (result.code === 200 && result.data) {
        const draft = result.data;
        draftId = draft.id;
        
        document.getElementById('categoryId').value = draft.categoryId || '';
        document.getElementById('title').value = draft.title || '';
        document.getElementById('content').value = draft.content || '';
        document.getElementById('gradYear').value = draft.gradYear || '';
        
        // 加载图片
        if (draft.imageUrl) {
            document.getElementById('imageUrl').value = draft.imageUrl;
            // 显示预览
            const preview = document.getElementById('imagePreview');
            preview.innerHTML = `
                <div style="position: relative; display: inline-block;">
                    <img src="${draft.imageUrl}" alt="预览" class="image-preview" style="max-width: 300px; max-height: 200px; border-radius: 4px;">
                    <button type="button" onclick="removeImage()" style="position: absolute; top: 5px; right: 5px; background: red; color: white; border: none; border-radius: 50%; width: 24px; height: 24px; cursor: pointer; font-size: 16px; line-height: 1;">×</button>
                </div>
            `;
        }
        
        if (draft.publishTime) {
            // 转换为本地时间格式 yyyy-MM-ddTHH:mm
            const time = new Date(draft.publishTime);
            const localTime = new Date(time.getTime() - time.getTimezoneOffset() * 60000)
                .toISOString().slice(0, 16);
            document.getElementById('publishTime').value = localTime;
        }
        
        showHint('已加载草稿', 'info');
    } else {
        // 没有草稿，使用用户默认届数
        const user = getUser();
        if (user && user.defaultGradYear) {
            document.getElementById('gradYear').value = user.defaultGradYear;
        }
    }
    
    // 加载上次的届数选择（优先级低于草稿和用户设置）
    lastGradYear = localStorage.getItem('lastGradYear');
    if (lastGradYear && !document.getElementById('gradYear').value) {
        document.getElementById('gradYear').value = lastGradYear;
    }
}

// 自动保存草稿（防抖）
function scheduleAutoSave() {
    clearTimeout(autoSaveTimer);
    autoSaveTimer = setTimeout(saveDraft, 8000); // 8秒后保存
}

// 保存草稿
async function saveDraft() {
    const title = document.getElementById('title').value.trim();
    const content = document.getElementById('content').value.trim();
    const categoryId = Number(document.getElementById('categoryId').value) || null;
    
    // 内容太少不保存
    if (!title && !content) {
        return;
    }
    
    // 草稿至少要有分类
    if (!categoryId) {
        showHint('请先选择分类', 'error');
        return;
    }
    
    const postData = {
        id: draftId,
        categoryId: categoryId,
        title: title,
        content: content,
        imageUrl: document.getElementById('imageUrl').value.trim() || null,
        gradYear: Number(document.getElementById('gradYear').value) || null,
        publishTime: document.getElementById('publishTime').value || null
    };
    
    const result = await fetchPost(API.POST_SAVE_DRAFT, postData);
    if (result.code === 200) {
        draftId = result.data.id;
        showHint('草稿已自动保存 ' + formatTime(new Date()), 'success');
    } else {
        showHint('自动保存失败: ' + result.message, 'error');
    }
}

// 显示提示信息
function showHint(message, type = 'success') {
    const hint = document.getElementById('autoSaveHint');
    hint.textContent = message;
    hint.style.color = type === 'success' ? '#28a745' : type === 'error' ? '#dc3545' : '#17a2b8';
}

// 发布帖子
async function publishPost(e) {
    e.preventDefault();
    
    const categoryId = Number(document.getElementById('categoryId').value);
    const title = document.getElementById('title').value.trim();
    const content = document.getElementById('content').value.trim();
    const imageUrl = document.getElementById('imageUrl').value.trim();
    const gradYear = Number(document.getElementById('gradYear').value) || null;
    const publishTimeInput = document.getElementById('publishTime').value;
    
    if (!categoryId) {
        alert('请选择分类');
        return;
    }
    
    if (!title) {
        alert('请输入标题');
        return;
    }
    
    if (!content) {
        alert('请输入内容');
        return;
    }
    
    // 保存届数选择到本地
    if (gradYear) {
        localStorage.setItem('lastGradYear', gradYear);
    }
    
    const postData = {
        id: draftId,
        categoryId: categoryId,
        title: title,
        content: content,
        imageUrl: imageUrl || null,
        gradYear: gradYear,
        publishTime: publishTimeInput || null
    };
    
    const result = await fetchPost(API.POST_PUBLISH, postData);
    if (result.code === 200) {
        alert(publishTimeInput ? '定时发布成功！' : '发布成功！');
        window.location.href = 'index.html';
    } else {
        alert('发布失败：' + result.message);
    }
}

// 手动保存草稿按钮
document.getElementById('saveDraftBtn').addEventListener('click', async () => {
    await saveDraft();
    alert('草稿已保存');
});

// 图片自动上传功能 - 监听文件选择事件
document.getElementById('imageUpload').addEventListener('change', async (e) => {
    const file = e.target.files[0];
    
    if (!file) {
        return;
    }
    
    // 检查文件类型
    if (!file.type.startsWith('image/')) {
        alert('只能上传图片文件');
        e.target.value = ''; // 清空选择
        return;
    }
    
    // 检查文件大小（5MB）
    if (file.size > 5 * 1024 * 1024) {
        alert('图片大小不能超过5MB');
        e.target.value = ''; // 清空选择
        return;
    }
    
    // 显示上传中状态
    const uploadProgress = document.getElementById('uploadProgress');
    uploadProgress.style.display = 'block';
    
    try {
        // 创建FormData
        const formData = new FormData();
        formData.append('file', file);
        
        // 发送上传请求
        const token = getToken();
        const response = await fetch(API.POST_UPLOAD_IMAGE, {
            method: 'POST',
            headers: {
                'Authorization': token
            },
            body: formData
        });
        
        const result = await response.json();
        
        if (result.code === 200) {
            // 保存图片URL
            const imageUrl = API_BASE_URL + result.data;
            document.getElementById('imageUrl').value = imageUrl;
            
            // 显示预览
            const preview = document.getElementById('imagePreview');
            preview.innerHTML = `
                <div style="position: relative; display: inline-block;">
                    <img src="${imageUrl}" alt="预览" style="max-width: 300px; max-height: 200px; border-radius: 4px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                    <button type="button" onclick="removeImage()" style="position: absolute; top: 5px; right: 5px; background: #dc3545; color: white; border: none; border-radius: 50%; width: 24px; height: 24px; cursor: pointer; font-size: 16px; line-height: 1; box-shadow: 0 2px 4px rgba(0,0,0,0.2);">×</button>
                </div>
            `;
            
            showHint('图片上传成功', 'success');
            uploadProgress.style.display = 'none';
        } else {
            alert('上传失败：' + result.message);
            uploadProgress.style.display = 'none';
            e.target.value = ''; // 清空选择
        }
    } catch (error) {
        console.error('上传错误:', error);
        alert('上传失败：' + error.message);
        uploadProgress.style.display = 'none';
        e.target.value = ''; // 清空选择
    }
});

// 删除图片
function removeImage() {
    document.getElementById('imageUrl').value = '';
    document.getElementById('imagePreview').innerHTML = '';
    document.getElementById('imageUpload').value = '';
    showHint('已删除图片', 'info');
}

// 初始化
document.addEventListener('DOMContentLoaded', async () => {
    // 检查登录
    if (!checkLogin()) {
        alert('请先登录');
        window.location.href = 'login.html';
        return;
    }
    
    // 更新用户链接
    const user = getUser();
    const userLink = document.getElementById('userLink');
    if (user) {
        userLink.innerHTML = `<a href="profile.html" style="margin-right: 10px;">${user.username}</a> | <a href="#" onclick="logout(); return false;">退出</a>`;
        userLink.href = '#';
    } else {
        userLink.textContent = '登录';
        userLink.href = 'login.html';
    }
    
    // 加载分类
    await loadCategories();
    
    // 加载草稿
    await loadDraft();
    
    // 监听表单提交
    document.getElementById('postForm').addEventListener('submit', publishPost);
    
    // 监听输入，自动保存草稿
    document.getElementById('title').addEventListener('input', scheduleAutoSave);
    document.getElementById('content').addEventListener('input', scheduleAutoSave);
    document.getElementById('categoryId').addEventListener('change', scheduleAutoSave);
    document.getElementById('gradYear').addEventListener('change', scheduleAutoSave);
    document.getElementById('imageUrl').addEventListener('input', scheduleAutoSave);
    document.getElementById('publishTime').addEventListener('change', scheduleAutoSave);
});
