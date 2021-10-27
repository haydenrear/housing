<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
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
    <!-- Google Maps API-->
    <script src="${pageContext.request.contextPath}/resources/javascript/index.js"></script>
    <title>Construction investment locator</title>
</head>
<body>
<header>
    <!-- Buttons on the right top  -->
    <div class ="container">
        <div class="row d-flex justify-content-center">
            <div class="col-xs-4">
                <img src="./pictures/Logo.png" id="logo" alt="Logo" width="55%"/>
            </div>
            <div class="col-xs-8" id="navigation">
                <nav class="navbar navbar-expand-sm navpan">
                    <ul class="navbar-nav">
                        <div><li class="nav-brand"><a class="nav-link h1c" href=#>Invest in Future!</a></li></div>
                        <div><li class="nav-item"><a class="nav-link h1c" href="${pageContext.request.contextPath}/">Home</a></li></div>
                        <div><li class="nav-item"><a class="nav-link h1c" href="#">About us</a></li></div>
                        <div><li class="nav-item"><a class="nav-link h1c" href="#">Contact us</a></li></div>
                    </ul>
                </nav>

            </div>
        </div>
    </div>
    </div>
    </div>
</header>
<h3>Google map window:</h3>

<!--The div element for the map -->
<div id="map"></div>

<footer>
    <p>@ This web site made by TFF Team in 2021</p>
</footer>

<!-- Async script executes immediately and must be after any DOM elements used in callback. -->
<script
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD6-iEqSntM5iCrI5Ucoj9XOTeqZ9qj8pE&callback=initMap&libraries=&v=weekly"
        async
></script>
</body>
</html>