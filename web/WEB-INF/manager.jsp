<%-- 
    Document   : manager
    Created on : 27.06.2016, 16:17:17
    Author     : pvr
--%>

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
        <jsp:useBean id="fileBean" type="ch.asridt.bean.FileRecordBean" scope="request" />
        
        <div class="container">
            <div class="page-header">
                <h1>Record Manager
                    <small class="hidden-xs">
                        ${fileBean.fiCount} FI files, 
                        ${fileBean.myCount} MY files
                    </small>
                </h1>
            </div>
            <!-- loop over recordgroups and items w/in each group -->        
            <c:forEach var="recordGroup" items="${fileBean.groups}">
                <div class="row">
                    <div class="col-sm-1">
                        <h3 class="group-header">${recordGroup.title}</h3>
                    </div>
                    <div class="col-sm-11">
                        <ul class="list-inline">
                            <c:forEach var="recordItem" items="${recordGroup.items}">
                                <li>
                                    <div>
                                        <a href="/WebRecordManager/RecordController?action=item&itemId=${recordItem.id}">
                                            <c:choose>
                                                <c:when test="${recordItem.type == 'my'}">
                                                    <img src="/WebRecordManager/images/my.jpg" class="thumbnail"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="/WebRecordManager/images/fi.jpg" class="thumbnail"/>                                                
                                                </c:otherwise>
                                            </c:choose>
                                            <h5>${recordItem.title}</h5>
                                        </a>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </c:forEach>
            <nav class="navbar navbar-inverse">
                <div class="container">
                    <ul class="nav navbar-nav">
                        <li class="active">
                            <a href="/WebRecordManager">Home</a>
                        </li>
                        <li>
                            <a href="/WebRecordManager/upload.html">Upload</a>
                        </li>
                        <li>
                            <a href="/WebRecordManager/RecordController?action=settings">Settings</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
        <script src="/WebRecordManager/js/jquery-1.11.1.min.js"></script>
        <script src="/WebRecordManager/js/bootstrap.min-js"></script>
    </body>
</html>
