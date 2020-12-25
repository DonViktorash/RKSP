<#import "base.ftl" as bs/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles.css">
    <title>О себе</title>
</head>
<body>
<@bs.navigationBar active="about"/>
<@bs.tableAbout
fullName="${fullName}"
group="${group}"
taskVariant="${taskVariant}"
taskText="${taskText}"
/>
</body>
</html>
