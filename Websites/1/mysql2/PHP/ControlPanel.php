<?php
mysql_connect('localhost', 'root', 'password');
if (!mysql_select_db('GameGrumps')) {
  echo '<li><a href="create.php">Create Database</a></li>';
}
else {
  echo '<li><a href="dropdatabase.php">Delete Database</a></li>';
}
?>
