// auth.js

// Lấy thông tin user từ localStorage
function getUser() {
    const user = localStorage.getItem("user");
    return user ? JSON.parse(user) : null;
}

// Kiểm tra login chung
function checkLogin() {
    const user = getUser();
    if (!user) {
        window.location.href = "/login.html";
        return false;
    }
    return true;
}

// Kiểm tra quyền truy cập trang
// requiredRoles = ["ADMIN", "CASHIER"] ...
function checkRole(requiredRoles = []) {
    const user = getUser();
    if (!user) {
        window.location.href = "/login.html";
        return false;
    }

    if (requiredRoles.length > 0 && !requiredRoles.includes(user.role)) {
        alert("⚠ Bạn không có quyền truy cập trang này!");
        window.location.href = "/login.html";
        return false;
    }

    return true;
}

// Logout
function logout() {
    localStorage.removeItem("user");
    window.location.href = "/login.html";
}

// Hàm helper để tự động redirect khi load trang
function authGuard(requiredRoles = []) {
    if (!checkLogin()) return;
    if (requiredRoles.length > 0) {
        checkRole(requiredRoles);
    }
}

// Exports nếu bạn dùng module (ES6)
// export { getUser, checkLogin, checkRole, logout, authGuard };
function authorizedFetch(url, options={}) {
    const token = localStorage.getItem("token");
    options.headers = {
        ...options.headers,
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    };
    return fetch(url, options).then(res => {
        if(res.status === 401) {
            alert("Phiên đăng nhập hết hạn!");
            window.location.href = "/login.html";
        }
        return res.json();
    });
}