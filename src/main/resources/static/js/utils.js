// 工具函数

// 获取token
function getToken() {
    return localStorage.getItem('token');
}

// 设置token
function setToken(token) {
    localStorage.setItem('token', token);
}

// 清除token
function clearToken() {
    localStorage.removeItem('token');
}

// 获取用户信息
function getUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

// 设置用户信息
function setUser(user) {
    localStorage.setItem('user', JSON.stringify(user));
}

// 清除用户信息
function clearUser() {
    localStorage.removeItem('user');
}

// 发起GET请求
async function fetchGet(url) {
    try {
        const token = getToken();
        const headers = {
            'Content-Type': 'application/json'
        };
        if (token) {
            headers['Authorization'] = token;
        }
        
        const response = await fetch(url, {
            method: 'GET',
            headers: headers
        });
        
        return await response.json();
    } catch (error) {
        console.error('GET请求失败:', error);
        return { code: 500, message: '网络请求失败' };
    }
}

// 发起POST请求
async function fetchPost(url, data) {
    try {
        const token = getToken();
        const headers = {
            'Content-Type': 'application/json'
        };
        if (token) {
            headers['Authorization'] = token;
        }
        
        const response = await fetch(url, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(data)
        });
        
        return await response.json();
    } catch (error) {
        console.error('POST请求失败:', error);
        return { code: 500, message: '网络请求失败' };
    }
}

// 发起PUT请求
async function fetchPut(url, data) {
    try {
        const token = getToken();
        const headers = {
            'Content-Type': 'application/json'
        };
        if (token) {
            headers['Authorization'] = token;
        }
        
        const response = await fetch(url, {
            method: 'PUT',
            headers: headers,
            body: JSON.stringify(data)
        });
        
        return await response.json();
    } catch (error) {
        console.error('PUT请求失败:', error);
        return { code: 500, message: '网络请求失败' };
    }
}

// 发起DELETE请求
async function fetchDelete(url) {
    try {
        const token = getToken();
        const headers = {
            'Content-Type': 'application/json'
        };
        if (token) {
            headers['Authorization'] = token;
        }
        
        const response = await fetch(url, {
            method: 'DELETE',
            headers: headers
        });
        
        return await response.json();
    } catch (error) {
        console.error('DELETE请求失败:', error);
        return { code: 500, message: '网络请求失败' };
    }
}

// 格式化时间
function formatTime(timeStr) {
    const date = new Date(timeStr);
    const now = new Date();
    const diff = now - date;
    
    const minute = 60 * 1000;
    const hour = 60 * minute;
    const day = 24 * hour;
    
    if (diff < minute) {
        return '刚刚';
    } else if (diff < hour) {
        return Math.floor(diff / minute) + '分钟前';
    } else if (diff < day) {
        return Math.floor(diff / hour) + '小时前';
    } else if (diff < 30 * day) {
        return Math.floor(diff / day) + '天前';
    } else {
        return date.toLocaleDateString();
    }
}

// 检查登录状态
function checkLogin() {
    const token = getToken();
    const user = getUser();
    return token && user;
}

// 跳转到登录页
function goToLogin() {
    window.location.href = 'login.html';
}

// 退出登录
function logout() {
    clearToken();
    clearUser();
    window.location.href = 'index.html';
}
