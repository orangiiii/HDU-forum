// 个人信息页面逻辑

// 加载用户信息
async function loadUserInfo() {
    const user = getUser();
    if (!user) {
        alert('请先登录');
        window.location.href = 'login.html';
        return;
    }
    
    // 获取最新的用户信息
    const result = await fetchGet(API.USER_INFO);
    if (result.code === 200) {
        const userInfo = result.data;
        
        // 更新本地存储
        setUser(userInfo);
        
        // 显示基本信息
        document.getElementById('username').textContent = userInfo.username;
        document.getElementById('createTime').textContent = formatTime(userInfo.createTime);
        
        // 填充设置表单
        if (userInfo.defaultGradYear) {
            document.getElementById('defaultGradYear').value = userInfo.defaultGradYear;
        }
    } else {
        alert('获取用户信息失败：' + result.message);
    }
}

// 更新右上角用户状态
function updateUserStatus() {
    const user = getUser();
    const userLink = document.getElementById('userLink');
    if (user) {
        userLink.innerHTML = `<span style="margin-right: 10px;">${user.username}</span> | <a href="#" onclick="logout(); return false;">退出</a>`;
        userLink.href = '#';
    }
}

// 初始化
document.addEventListener('DOMContentLoaded', async () => {
    // 检查登录状态
    if (!checkLogin()) {
        alert('请先登录');
        window.location.href = 'login.html';
        return;
    }
    
    // 更新用户状态
    updateUserStatus();
    
    // 加载用户信息
    await loadUserInfo();
    
    // 处理个人设置表单提交
    document.getElementById('profileForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const defaultGradYear = document.getElementById('defaultGradYear').value;
        
        const result = await fetchPut(API.USER_UPDATE_PROFILE, {
            defaultGradYear: defaultGradYear ? parseInt(defaultGradYear) : null
        });
        
        if (result.code === 200) {
            alert('设置保存成功');
            // 更新本地存储的用户信息
            setUser(result.data);
        } else {
            alert('保存失败：' + result.message);
        }
    });
    
    // 处理修改密码表单提交
    document.getElementById('passwordForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const oldPassword = document.getElementById('oldPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        
        // 验证新密码
        if (newPassword.length < 6) {
            alert('新密码长度至少6位');
            return;
        }
        
        if (newPassword !== confirmPassword) {
            alert('两次输入的新密码不一致');
            return;
        }
        
        const result = await fetchPost(API.USER_CHANGE_PASSWORD, {
            oldPassword: oldPassword,
            newPassword: newPassword
        });
        
        if (result.code === 200) {
            alert('密码修改成功，请重新登录');
            // 清除登录信息，跳转到登录页
            clearToken();
            clearUser();
            window.location.href = 'login.html';
        } else {
            alert('修改失败：' + result.message);
        }
    });
});
