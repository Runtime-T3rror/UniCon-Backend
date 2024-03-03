<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Setup</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center mt-5">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title">Password Setup</h2>
                    <form action="${pageContext.request.contextPath}/v1/auth/set-password" method="post">
                        <input type="hidden" name="token" value="${param.token}">
                        <div class="form-group">
                            <label for="password">Password:</label>
                            <input type="password" id="password" name="password" class="form-control" pattern=".{8,}"
                                   title="Password must be at least 8 characters long" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Set Password</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
