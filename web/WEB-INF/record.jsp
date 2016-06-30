<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
        <jsp:useBean id="itemBean" type="ch.asridt.record.RecordItem" scope="request"/>
        <div class="container">
            <div class="page-header">
                <h1>Record Manager</h1>
            </div>
            <h4>${itemBean.title}</h4>
            <c:choose>
                <c:when test="${itemBean.type == 'my'}">
                    <img src="/WebRecordManager/images/my.jpg" class="media-large-image"/>
                </c:when>  
                <c:when test="${itemBean.type == 'fi'}">
                    <img src="/WebRecordManager/images/fi.jpg" class="media-large-image"/>
                </c:when>
                <c:otherwise>
                    <h2 style="text-align: center">This image type is not supported</h2>
                </c:otherwise>
            </c:choose>
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
