// 首页逻辑

// 加载分类列表
async function loadCategories() {
    const result = await fetchGet(API.CATEGORY_LIST);
    if (result.code === 200) {
        const categoryList = document.getElementById('categoryList');
        categoryList.innerHTML = result.data.map(cat => `
            <li><a href="?categoryId=${cat.id}">${cat.name}</a></li>
        `).join('');
    }
}

// 加载帖子列表
async function loadPosts() {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryId = urlParams.get('categoryId');
    
    let url = API.POST_LIST;
    if (categoryId) {
        url = API.POST_BY_CATEGORY(categoryId);
    }
    
    const result = await fetchGet(url);
    if (result.code === 200) {
        const postList = document.getElementById('postList');
        if (result.data.length === 0) {
            postList.innerHTML = '<div class="empty">暂无帖子</div>';
            return;
        }
        
        postList.innerHTML = result.data.map(post => `
            <div class="post-item">
                <div class="post-header">
                    <h3><a href="detail.html?id=${post.id}">${post.title}</a></h3>
                    <span class="category-tag">${post.categoryName}</span>
                </div>
                <div class="post-meta">
                    <span>作者: ${post.username}</span>
                    <span>浏览: ${post.viewCount}</span>
                    <span>评论: ${post.commentCount}</span>
                    <span>点赞: ${post.likeCount}</span>
                    <span>${formatTime(post.createTime)}</span>
                </div>
            </div>
        `).join('');
    }
}

// 初始化页面
document.addEventListener('DOMContentLoaded', async () => {
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
    
    // 发布帖子按钮
    document.getElementById('createPostBtn').onclick = () => {
        if (!checkLogin()) {
            alert('请先登录');
            goToLogin();
            return;
        }
        // 跳转到发帖页面
        window.location.href = 'create_post.html';
    };
    
    // 加载数据
    await loadCategories();
    await loadPosts();
});
