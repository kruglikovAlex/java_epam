<html>
<body>
<form action="/submitData" method="post">
    <label path="login">Login:</label><input type="text" name="login"/><br/>
    <label path="name">Name:</label><input type="text" name="name"/><br/>
    <input type="submit" name="Submit">
</form>
<script>
function mySubmit(url)
{
document.forms[0].action=url;
document.forms[0].submit();
}
</script>
</body>
</html>