<#import "base.ftl" as bs/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <script src="task.js"></script>
    <link rel="stylesheet" href="styles.css">
    <title>Задание</title>
</head>
<body>
<@bs.navigationBar active="task"/>
<@bs.taskForm
lastTaskRequest="${lastTaskRequest}"
lastTaskResponse="${lastTaskResponse}"
/>
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
</body>
</html>
