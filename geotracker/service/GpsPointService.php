<?php
include_once 'repository/IRepository.php';
include_once 'model/GpsPoint.php';
include_once 'db/DbLink.php';

class GpsPointService implements IRepository {
    private $db;

    public function __construct() {
        $this->db = new DbLink();
    }

    public function create($point) {
        $sql = "INSERT INTO gps_record (latitude, longitude, captured_at, device_id)
                VALUES (:lat, :lng, :captured_at, :device_id)";

        $stmt = $this->db->getConn()->prepare($sql);
        $stmt->execute([
            ':lat'         => $point->getLat(),
            ':lng'         => $point->getLng(),
            ':captured_at' => $point->getCapturedAt(),
            ':device_id'   => $point->getDeviceId()
        ]);
    }

    public function update($obj)    {}
    public function delete($obj)    {}
    public function findById($id)   {}
    public function findAll()       {}
}
?>