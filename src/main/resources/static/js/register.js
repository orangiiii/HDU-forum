// 注册逻辑

document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    const result = await fetchPost(API.USER_REGISTER, {
        username: username,
        password: password
    });
    
    if (result.code === 200) {
        alert('注册成功，请登录');
        window.location.href = 'login.html';
    } else {
        alert(result.message);
    }
});
