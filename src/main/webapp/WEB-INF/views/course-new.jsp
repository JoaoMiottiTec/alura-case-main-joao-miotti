<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>New Course</title></head>
<body>
<h1>New Course</h1>

<c:if test="${not empty error}">
  <p style="color:red">${error}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/admin/course/new">
  <div>
    <label>Name</label><br/>
    <input type="text" name="name" value="${form.name}" required/>
  </div>

  <div>
    <label>Code (slug)</label><br/>
    <input type="text" name="code" value="${form.code}" placeholder="java-web" required/>
  </div>

  <div>
    <label>Instructor Email</label><br/>
    <input type="email" name="instructorEmail" value="${form.instructorEmail}" required/>
  </div>

  <div>
    <label>Category</label><br/>
    <select name="categoryId" required>
      <c:forEach var="cat" items="${categories}">
        <option value="${cat.id}">${cat.name}</option>
      </c:forEach>
    </select>
  </div>

  <div>
    <label>Description</label><br/>
    <textarea name="description" rows="4" cols="60">${form.description}</textarea>
  </div>

  <br/>
  <button type="submit">Create</button>
</form>

<p><a href="${pageContext.request.contextPath}/admin/courses">Back to list</a></p>
</body>
</html>
