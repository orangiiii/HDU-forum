// 帖子详情逻辑

let currentPostId = null;

// 加载帖子详情
async function loadPostDetail() {
    const urlParams = new URLSearchParams(window.location.search);
    currentPostId = urlParams.get('id');
    
    if (!currentPostId) {
        alert('帖子ID无效');
        window.location.href = 'index.html';
        return;
    }
    
    const result = await fetchGet(API.POST_DETAIL(currentPostId));
    if (result.code === 200) {
        const post = result.data;
        document.getElementById('postDetail').innerHTML = `
            <h2>${post.title}</h2>
            <div class="post-meta">
                <span>作者: ${post.username}</span>
                <span>分类: ${post.categoryName}</span>
                <span>浏览: ${post.viewCount}</span>
                <span>点赞: ${post.likeCount}</span>
                <span>${formatTime(post.createTime)}</span>
            </div>
            <div class="post-content">${post.content}</div>
            <div class="post-actions">
                <button onclick="likePost()" class="btn">点赞</button>
            </div>
        `;
    }
}

// 加载评论列表
async function loadComments() {
    const result = await fetchGet(API.COMMENT_BY_POST(currentPostId));
    if (result.code === 200) {
        const commentList = document.getElementById('commentList');
        if (result.data.length === 0) {
            commentList.innerHTML = '<div class="empty">暂无评论</div>';
            return;
        }
        
        commentList.innerHTML = result.data.map(comment => `
            <div class="comment-item">
                <div class="comment-header">
                    <strong>${comment.username}</strong>
                    <span>${formatTime(comment.createTime)}</span>
                </div>
                <div class="comment-content">${comment.content}</div>
                <div class="comment-actions">
                    <button onclick="likeComment(${comment.id})" class="btn-text">点赞 ${comment.likeCount}</button>
                </div>
            </div>
        `).join('');
    }
}

// 发表评论
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('submitComment').onclick = async () => {
        if (!checkLogin()) {
            alert('请先登录');
            goToLogin();
            return;
        }
        
        const content = document.getElementById('commentContent').value.trim();
        if (!content) {
            alert('请输入评论内容');
            return;
        }
        
        const result = await fetchPost(API.COMMENT_CREATE, {
            postId: currentPostId,
            content: content
        });
        
        if (result.code === 200) {
            alert('评论成功');
            document.getElementById('commentContent').value = '';
            loadComments();
        } else {
            alert(result.message);
        }
    };
    
    // 加载数据
    loadPostDetail();
    loadComments();
    
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
});

// 点赞帖子
async function likePost() {
    if (!checkLogin()) {
        alert('请先登录');
        goToLogin();
        return;
    }
    
    const result = await fetchPost(API.POST_LIKE(currentPostId), {});
    if (result.code === 200) {
        alert('点赞成功');
        loadPostDetail();
    } else {
        alert(result.message);
    }
}

// 点赞评论
async function likeComment(commentId) {
    if (!checkLogin()) {
        alert('请先登录');
        goToLogin();
        return;
    }
    
    const result = await fetchPost(API.COMMENT_LIKE(commentId), {});
    if (result.code === 200) {
        alert('点赞成功');
        loadComments();
    } else {
        alert(result.message);
    }
}
