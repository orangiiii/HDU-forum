// 登录逻辑

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    const result = await fetchPost(API.USER_LOGIN, {
        username: username,
        password: password
    });
    
    if (result.code === 200) {
        setToken(result.data.token);
        setUser(result.data.user);
        alert('登录成功');
        window.location.href = 'index.html';
    } else {
        alert(result.message);
    }
});
