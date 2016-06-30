<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="/WebRecordManager/css/bootstrap.min.css" rel="stylesheet">
        <link href="/WebRecordManager/css/media-manager.css" rel="stylesheet">
        <title>Record Manager</title>
    </head>
    <body>
        <jsp:useBean id="error" type="java.lang.String" scope="request" />
        <div class="container">
            <div class="page-header">
                <h1>Record Manager <small class="hidden-xs">- Error</small></h1>
            </div>
            <h2>${error}</h2>
            <nav class="navbar navbar-inverse">
                <div class="container">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="/WebRecordManager">Home</a></li>
                    </ul>
                </div>
            </nav>
        </div>
    </body>
</html>
