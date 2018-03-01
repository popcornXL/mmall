<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<body>
<h2>Hello World!</h2>

<%--springmvc上傳文件--%>
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc上傳文件" />
</form>


<%--副文本上傳--%>
<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="副文本上傳" />
</form>
</body>
</html>
