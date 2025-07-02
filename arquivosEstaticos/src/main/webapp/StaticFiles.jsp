<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="static/css/index.css">
    <title>Document</title>
</head>
<body>
    <div>
        <a href="static/pdf/Minuta-Padrao.pdf" target="_blank" rel="noopener noreferrer">View PDF</a>
    </div>
    <div>
        <img src="static/img/dog.png" alt="dog" />
    </div>

    <form>
        <input type="text" name="search" placeholder="Search..">
    </form>

    <h1>People List</h1>
    <div id="pessoas">
        Loading...
    </div>

    <script src="static/js/index.js"></script>
    <script>
        buscarPessoas('pessoas');
    </script>
</body>
</html>