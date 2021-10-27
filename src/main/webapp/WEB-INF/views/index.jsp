<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- JS link -->

    <!-- BootStrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <!-- Popper JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <!-- BootStrap -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <!-- External CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/style.css">

    <title>Construction investment locator</title>
</head>
<body>
<header>
    </div>
    <div class="container">
        <div class="row d-flex justify-content-center">
            <div class="col-xs-4">
                <img src="${pageContext.request.contextPath}/resources/pictures/Logo.png" id="logo" alt="Logo"
                     width="55%"/>
            </div>
            <div class="col-xs-8" id="navigation">
                <nav class="navbar navbar-expand-sm navpan">
                    <ul class="navbar-nav">
                        <div>
                            <li class="nav-brand"><a class="nav-link h1c" href=#>Invest in Future!</a></li>
                        </div>
                        <div>
                            <li class="nav-item"><a class="nav-link h1c"
                                                    href="${pageContext.request.contextPath}/">Home</a></li>
                        </div>
                        <div>
                            <li class="nav-item"><a class="nav-link h1c" href="#">About us</a></li>
                        </div>
                        <div>
                            <li class="nav-item"><a class="nav-link h1c" href="#">Contact us</a></li>
                        </div>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
    </div>
    </div>
</header>

<!-- Carousel -->
<c:if test="${price == null}">
<div class="container">
    <div class="row d-flex justify-content-center">
        <div class="col-md-auto">
            <div id="demo" class="carousel slide" data-ride="carousel">
                <!-- Indicators -->
                <ul class="carousel-indicators">
                    <li data-target="#demo" data-slide-to="0" class="active"></li>
                    <li data-target="#demo" data-slide-to="1"></li>
                    <li data-target="#demo" data-slide-to="2"></li>
                </ul>

                <!-- The slideshow -->
                <div class="carousel-inner carousel2">
                    <div class="carousel-item active">
                        <img class="img-fluid" src="${pageContext.request.contextPath}/resources/pictures/Slide1.jpg"
                             alt="Responsive image" style="height:360px;">
                    </div>
                    <div class="carousel-item">
                        <img class="img-fluid" src="${pageContext.request.contextPath}/resources/pictures/Slide2.jpg"
                             alt="Responsive image" style="height:360px;">
                    </div>
                    <div class="carousel-item">
                        <img class="img-fluid" src="${pageContext.request.contextPath}/resources/pictures/Slide3.jpg"
                             alt="Responsive image" style="height:360px;">
                    </div>
                </div>

                <!-- Left and right controls -->
                <a class="carousel-control-prev" href="#demo" data-slide="prev">
                    <span class="carousel-control-prev-icon"></span>
                </a>
                <a class="carousel-control-next" href="#demo" data-slide="next">
                    <span class="carousel-control-next-icon"></span>
                </a>
            </div>
        </div>
    </div>
</div>
</c:if>
<br>
<div class="container">
    <div class="row d-flex justify-content-center">
        <c:if test="${price == null}">
        <div class="col-xs-12">
            <h2>Please provide state and city for getting result</h2>
            <form action="suggestion" method="post" modelAtribute="result">
                <div class="form-group">
                    <label for="state">Country:</label>
                    <input type="text" class="form-control" name="state" id="state"/>
                </div>
                <div class="form-group">
                    <label for="city">City:</label>
                    <input type="text" class="form-control" name="city" id="city"/>
                </div>
                <div class="form-group">
                    <label for="sel1">Select property type (select one):</label>
                    <select class="form-control" id="sel1">
                        <option>Commercial</option>
                        <option>Residential</option>
                    </select>
                    <div class="d-flex justify-content-center"><input type="submit" class="btn btn-primary" value="submit"></div>
                </div>
            </form>
        </div>
        </c:if>
        <c:if test="${price != null}">
            <div>
                <h2>Address</h2>
                <h3>${address}</h3>
                <h2>Projected Return</h2>
                <h3>${returnRate}</h3>
            </div>
        </c:if>
    </div>
</div>
</div>
<br>
<br>
<br>
<c:if test="${price == null}">
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <div class="d-flex justify-content-center text-center">
                <div>
                    <p>Want to invest in real estate building, but can't determine with the place?</p>
                    <img class="mx-auto w-100 text-center"
                         src="https://www.qmswrapper.com/files/Pictures/blog/Doc_in_a_paper_office/documents_in_a_paperless_office_cover2.gif"
                         alt="Responsive image"/>
                </div>
            </div>
            <br>
            <div class="d-flex mx-auto justify-content-center text-center">
                <u>Our locator would help you, just specify state and city and we'll show you the most promising
                    points!</u>
            </div>
        </div>
    </div>
</div>
</c:if>
</br>
</br>
<br>
<footer>
    <p>@ This web site made by TFF Team in 2021</p>
</footer>
</body>

</html>