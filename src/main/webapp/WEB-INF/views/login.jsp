<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="/assets/css/login.css">
</head>
<body class="login-page">
<div class="container">
    <div class="login-box">
        <h2>Já estuda com a gente?</h2>
        <p>Faça seu login e boa aula!</p>
        <a href="/admin/categories" class="btn-login">ENTRAR</a>
    </div>

    <div class="courses">
        <h2>Ainda não estuda com a gente?</h2>
        <p>São mais de mil cursos nas seguintes áreas:</p>

<%--    TODO: Implementar a Questão 3 (As informações devem vir do seu banco de dados)  --%>
        <div class="grid">
            <div class="card"><h3>Escola_ PROGRAMAÇÃO</h3><p>Lógica de programação, .NET, Automação e Produtividade</p></div>
            <div class="card"><h3>Escola_ FRONT-END</h3><p>HTML, CSS, Svelte, VueJS</p></div>
            <div class="card"><h3>Escola_ DATA SCIENCE</h3><p>SQL e Banco de Dados, Engenharia de Dados, Análise de dados</p></div>
            <div class="card"><h3>Escola_ INTELIGÊNCIA ARTIFICIAL</h3><p>IA para Criativos, IA para Programação, IA para Negócios</p></div>
            <div class="card"><h3>Escola_ DEVOPS</h3><p>Linux, FinOps, Automação de processos</p></div>
            <div class="card"><h3>Escola_ UX & DESIGN</h3><p>UI Design, Design System, UX Writing</p></div>
            <div class="card"><h3>Escola_ MOBILE</h3><p>Flutter, Android, iOS</p></div>
            <div class="card"><h3>Escola_ INOVAÇÃO & GESTÃO</h3><p>Agilidade, Liderança, Ensino e Aprendizagem</p></div>
        </div>
    </div>
</div>
</body>
</html>
