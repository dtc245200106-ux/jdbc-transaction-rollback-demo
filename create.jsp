<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Thêm mới User</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; font-weight: bold; margin-bottom: 5px; }
        input[type="text"], input[type="email"] {
            width: 300px; padding: 8px; border: 1px solid #ccc; border-radius: 4px;
        }
        button { padding: 10px 20px; background: #007bff; color: white;
                 border: none; border-radius: 4px; cursor: pointer; }
        button:hover { background: #0056b3; }
        a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>
    <h1>Thêm mới User</h1>
    <form method="post" action="users?action=create">
        <div class="form-group">
            <label>Tên:</label>
            <input type="text" name="name" required>
        </div>

        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" required>
        </div>

        <div class="form-group">
            <label>Quốc gia:</label>
            <input type="text" name="country" required>
        </div>

        <div class="form-group">
            <label>Quyền hạn (Permissions):</label>
            <div style="display: flex; gap: 15px; margin-top: 5px;">
                <label><input type="checkbox" name="permissions" value="1"> Thêm (Add)</label>
                <label><input type="checkbox" name="permissions" value="2"> Sửa (Edit)</label>
                <label><input type="checkbox" name="permissions" value="3"> Xoá (Delete)</label>
                <label><input type="checkbox" name="permissions" value="4"> Xem (View)</label>
            </div>
        </div>

        <button type="submit">Lưu thông tin</button>
        <a href="users" style="margin-left: 15px;">Quay lại danh sách</a>
    </form>
</body>
</html>