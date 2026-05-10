<?php
class DbLink {
    private $conn;

    public function __construct() {
        $host   = 'localhost';
        $dbname = 'geotracker';
        $user   = 'root';
        $pass   = '';

        try {
            $this->conn = new PDO(
                "mysql:host=$host;dbname=$dbname;charset=utf8mb4",
                $user,
                $pass
            );
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (Exception $e) {
            die('Connection error: ' . $e->getMessage());
        }
    }

    public function getConn() {
        return $this->conn;
    }
}
?>