<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Courses</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/courses.css">
  <style>
    .nowrap { white-space: nowrap; }
    .modal .help { font-size:12px; color:#777; margin-top:6px; }
  </style>
</head>
<body>

<div class="container">
  <div class="page-header">
    <h1>Courses</h1>
  </div>

  <p><a class="btn btn-info" href="${pageContext.request.contextPath}/admin/course/new">New Course</a></p>

  <c:if test="${empty courses}">
    <div class="alert alert-info">No courses found.</div>
  </c:if>

  <c:if test="${not empty courses}">
    <table class="table table-hover">
      <thead>
        <tr>
          <th>ID</th><th>Name</th><th>Code</th><th>Instructor</th><th>Category</th><th>Status</th><th class="text-right">Actions</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach var="c" items="${courses}">
        <tr>
          <td>${c.id}</td>
          <td>${c.name}</td>
          <td><span class="label label-default">${c.code}</span></td>
          <td>${c.instructorEmail}</td>
          <td>${c.categoryName}</td>
          <td>
            <c:choose>
              <c:when test="${c.status == 'ACTIVE'}"><span class="label label-success">ACTIVE</span></c:when>
              <c:otherwise><span class="label label-default">INACTIVE</span></c:otherwise>
            </c:choose>
          </td>
          <td class="text-right nowrap">
            <button type="button" class="btn btn-primary"
                    data-toggle="modal" data-target="#editCourseModal"
                    data-code="${c.code}"
                    data-name="${c.name}"
                    data-instructor="${c.instructorEmail}"
                    data-category-id="${c.categoryId}"
                    data-description="${c.description}"
                    data-status="${c.status}">
              Edit
            </button>

            <form method="post" action="${pageContext.request.contextPath}/course/${c.code}/inactive" style="display:inline">
              <button type="submit" class="btn btn-warning">Inactivate</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>
</div>

<div class="modal fade" id="editCourseModal" tabindex="-1" role="dialog" aria-labelledby="editCourseLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <form id="editCourseForm" method="post" action="">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
          <h4 class="modal-title" id="editCourseLabel">Edit Course</h4>
        </div>

        <div class="modal-body">
          <div class="form-group">
            <label>Name</label>
            <input type="text" class="form-control" name="name" id="courseName" required maxlength="100">
          </div>

          <div class="form-group">
            <label>Code</label>
            <input type="text" class="form-control" id="courseCode" disabled>
            <div class="help">The code is immutable.</div>
          </div>

          <div class="form-group">
            <label>Instructor (email)</label>
            <input type="email" class="form-control" name="instructorEmail" id="courseInstructor" required>
          </div>

          <div class="form-group">
            <label>Category</label>
            <select class="form-control" name="categoryId" id="courseCategory" required>
              <c:forEach var="cat" items="${categories}">
                <option value="${cat.id}">${cat.name}</option>
              </c:forEach>
            </select>
          </div>

          <div class="form-group">
            <label>Description</label>
            <textarea class="form-control" name="description" id="courseDescription" rows="3" required minlength="10" maxlength="5000"></textarea>
          </div>
          <input type="hidden" name="status" id="courseStatusHidden" value="ACTIVE"/>

        <div class="form-group">
          <label>Status</label>
          <div class="status-toggle">
            <span id="labelActive" class="status-label active">ACTIVE</span>
          
            <label class="switch">
              <input type="checkbox" id="courseStatusToggle">
              <span class="slider"></span>
            </label>
          
            <span id="labelInactive" class="status-label inactive">INACTIVE</span>
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
          <button type="submit" class="btn btn-primary">Save</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script>
  window.jQuery || document.write('<script src="/assets/external-libs/jquery/jquery-1.12.4.min.js"><\\/script>');
</script>
<script src="/assets/external-libs/bootstrap/js/bootstrap.min.js"></script>

<script>
(function () {
  var base = '${pageContext.request.contextPath}';
  function setStatusUI(isInactive) {
    $('#courseStatusToggle').prop('checked', isInactive);
    $('#courseStatusHidden').val(isInactive ? 'INACTIVE' : 'ACTIVE');
    $('#labelActive').toggleClass('active', !isInactive);
    $('#labelInactive').toggleClass('active', isInactive);
  }

  $('#editCourseModal').on('show.bs.modal', function (event) {
    var btn = $(event.relatedTarget);

    var code        = btn.data('code');
    var name        = btn.data('name');
    var instructor  = btn.data('instructor');
    var categoryId  = btn.data('category-id');
    var description = btn.data('description');
    var status      = btn.data('status');

    var modal = $(this);
    modal.find('#editCourseForm').attr('action', base + '/admin/course/' + code + '/edit');

    modal.find('#courseCode').val(code);
    modal.find('#courseName').val(name);
    modal.find('#courseInstructor').val(instructor);
    modal.find('#courseDescription').val(description);
    modal.find('#courseCategory').val(categoryId);

    setStatusUI(status === 'INACTIVE');
  });

  $(document)
    .off('change', '#courseStatusToggle')
    .on('change', '#courseStatusToggle', function () {
      setStatusUI($(this).is(':checked'));
    });

})();
</script>

</body>
</html>