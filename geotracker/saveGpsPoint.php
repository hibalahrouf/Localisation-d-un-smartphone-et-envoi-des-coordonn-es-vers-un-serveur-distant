<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once 'service/GpsPointService.php';
    handleIncoming();
}

function handleIncoming() {
    $lat       = $_POST['latitude'];
    $lng       = $_POST['longitude'];
    $timestamp = $_POST['captured_at'];
    $devId     = $_POST['device_id'];

    $svc   = new GpsPointService();
    $point = new GpsPoint(null, $lat, $lng, $timestamp, $devId);
    $svc->create($point);

    echo "GPS point saved successfully";
}
?>