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
        <jsp:useBean id="uploadData" type="ch.asridt.model.UploadData" scope="request" />
        <div class="container">
            <div class="page-header">
                <h1>Record Manager <small class="hidden-xs">- Upload Data</small></h1>
            </div>
            <p>File name: ${uploadData.fileName}</p>
            <p>Title:     ${uploadData.title}</p>
            <p>Extension: ${uploadData.extension}</p>
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
