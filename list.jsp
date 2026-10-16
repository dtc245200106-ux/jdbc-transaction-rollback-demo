<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh sách User</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background: #007bff; color: white; }
        tr:nth-child(even) { background: #f2f2f2; }
        a { color: #007bff; text-decoration: none; margin-right: 10px; }
        .btn-create { display: inline-block; padding: 10px 20px; background: #28a745;
                      color: white; border-radius: 4px; margin-bottom: 15px; }
    </style>
</head>
<body>
    <h1>Danh sách User</h1>
    <a href="users?action=create" class="btn-create">+ Thêm User mới</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Email</th>
                <th>Quốc gia</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${listUsers}">
                <tr>
                    <td><c:out value="${user.id}"/></td>
                    <td><c:out value="${user.name}"/></td>
                    <td><c:out value="${user.email}"/></td>
                    <td><c:out value="${user.country}"/></td>
                    <td>
                        <a href="users?action=edit&id=${user.id}">Sửa</a>
                        <a href="users?action=delete&id=${user.id}"
                           onclick="return confirm('Bạn có chắc muốn xoá user này?');">Xoá</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>