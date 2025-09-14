<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Courses</title></head>
<body>
<h1>Courses</h1>

<p><a href="${pageContext.request.contextPath}/admin/course/new">New Course</a></p>

<c:if test="${empty courses}">
  <p>No courses found.</p>
</c:if>

<c:if test="${not empty courses}">
  <table border="1" cellpadding="6">
    <tr>
      <th>ID</th><th>Name</th><th>Code</th><th>Instructor</th><th>Category</th><th>Status</th><th>Actions</th>
    </tr>
    <c:forEach var="c" items="${courses}">
      <tr>
        <td>${c.id}</td>
        <td>${c.name}</td>
        <td>${c.code}</td>
        <td>${c.instructorEmail}</td>
        <td>${c.categoryName}</td>
        <td>${c.status}</td>
        <td>
          <form method="post" action="${pageContext.request.contextPath}/course/${c.code}/inactive" style="display:inline">
            <button type="submit">Inactivate</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
</c:if>
</body>
</html>
