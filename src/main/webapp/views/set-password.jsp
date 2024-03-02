<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Password Setup</title>
    <style>
        body {
            background-color: #eee;
            color: #222831;
            font-family: 'Montserrat', 'sans-serif';
            justify-content: center;
            display: flex;
        }

        button {
            margin: 1rem;
            background-color: #00adb5;
        }
    </style>
</head>
<body>
<h2>Password Setup</h2>
<form action="${pageContext.request.contextPath}/v1/auth/set-password" method="post">
    <input type="hidden" name="token" value="${param.token}">
    <label for="password">Password:</label><br>
    <input type="password" id="password" name="password" required><br>
    <button type="submit">Set Password</button>
</form>
</body>
</html>
