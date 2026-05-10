<?php
class GpsPoint {
    private $pointId;
    private $lat;
    private $lng;
    private $capturedAt;
    private $deviceId;

    public function __construct($pointId, $lat, $lng, $capturedAt, $deviceId) {
        $this->pointId    = $pointId;
        $this->lat        = $lat;
        $this->lng        = $lng;
        $this->capturedAt = $capturedAt;
        $this->deviceId   = $deviceId;
    }

    // --- Getters ---
    public function getPointId()    { return $this->pointId;    }
    public function getLat()        { return $this->lat;        }
    public function getLng()        { return $this->lng;        }
    public function getCapturedAt() { return $this->capturedAt; }
    public function getDeviceId()   { return $this->deviceId;   }

    // --- Setters ---
    public function setPointId($v)    { $this->pointId    = $v; }
    public function setLat($v)        { $this->lat        = $v; }
    public function setLng($v)        { $this->lng        = $v; }
    public function setCapturedAt($v) { $this->capturedAt = $v; }
    public function setDeviceId($v)   { $this->deviceId   = $v; }
}
?>