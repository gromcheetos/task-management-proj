document.addEventListener('DOMContentLoaded', (event) => {
  $(document).ready(function () {
    $('#addTaskModal').modal('hide');

    $('.add-task-btn').click(function () {
      $('#addTaskModal').modal('show');
    });

    $('#userSelect').on('change', function() {
        let selectedText = $('#userSelect option:selected').text();
        $('#userInput').val(selectedText);
    });

    $("#allPriority").click(function (){
      var checked = $("#allPriority").is(":checked");
      if (checked) {
        $("input[name='priority']").prop('checked', true);
        $("#allPriority").prop('checked', true);
      }else if(!checked){
        $("input[name='priority']").prop('checked', false);
      }
    });

  });
  const boardFilterForm = document.getElementById("boardFilter");
  if (boardFilterForm) {
    boardFilterForm.addEventListener("submit", function (event) {
      event.preventDefault();
      filterBoardsAndTasks();
    });
  }
});

function toggleDropdown(dropdownId) {
  var dropdown = document.getElementById(dropdownId);
  dropdown.classList.toggle('show');

  document.addEventListener('click', function handleClickOutside(event) {
    if (!dropdown.contains(event.target) &&
        !event.target.closest('.dropdown-toggle')) {
      dropdown.classList.remove('show');
      document.removeEventListener('click', handleClickOutside);
    }
  });
}

function filterBoardsAndTasks() {
  const allBoardsCheckbox = document.querySelector(
      "input[name='boardName'][value='all']");
  let boardNames = [];

  if (allBoardsCheckbox && allBoardsCheckbox.checked) {
    boardNames.push("all");
  } else {
    const boardNameInputs = document.querySelectorAll(
        "input[name='boardName']:checked");
    boardNames = Array.from(boardNameInputs).map(input => input.value);
  }

  // Collect priorities
  const allPrioritiesCheckbox = document.querySelector(
      "input[name='priority'][value='all']");
  let priorities = [];

  if (allPrioritiesCheckbox && allPrioritiesCheckbox.checked) {
    priorities.push("all");
  } else {
    const priorityInputs = document.querySelectorAll(
        "input[name='priority']:checked");
    priorities = Array.from(priorityInputs).map(input => input.value);
  }

  const userId = $("#userId").val();
  const projectId = $("#projectId").val();

  const params = new URLSearchParams();
  boardNames.forEach(name => params.append("boardName", name));
  priorities.forEach(priority => params.append("priority", priority));
  params.append("userId", userId);
  params.append("projectId", projectId)

  fetch(`/board/filter?${params.toString()}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'text/html'
    }
  })
  .then(response => {
    if (response.ok) {
      return response.text();
    }
    throw new Error('Network response was not ok.');
  })
  .then(html => {
    document.getElementById('boardList').innerHTML = html;
  })
  .catch(error => {
    console.error('There was a problem with the fetch operation:', error);
  });
}

