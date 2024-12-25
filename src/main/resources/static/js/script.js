document.addEventListener('DOMContentLoaded', (event) => {
  $(document).ready(function () {
    $('#addTaskModal').modal('hide');

    $('.add-task-btn').click(function () {
      $('#addTaskModal').modal('show');
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
}

window.onclick = function (event) {
  if (!event.target.closest('.custom-dropdown')) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    for (var i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}

// When a user checks All Boards, undo other option.
const allBoardsCheckbox = document.querySelector(
    "input[name='boardName'][value='all']");
const boardNameCheckboxes = document.querySelectorAll(
    "input[name='boardName']:not([value='all'])");

if (allBoardsCheckbox) {
  allBoardsCheckbox.addEventListener('change', function () {
    if (this.checked) {
      boardNameCheckboxes.forEach(checkbox => checkbox.checked = false);
    }
  });
}
boardNameCheckboxes.forEach(checkbox => {
  checkbox.addEventListener('change', function () {
    if (this.checked) {
      allBoardsCheckbox.checked = false;
    }
  });
});

// When a user checks All Priorities, undo other option.
const allTasksCheckbox = document.querySelector(
    "input[name='priority'][value='all']");
const priorityCheckboxes = document.querySelectorAll(
    "input[name='priority']:not([value='all'])");

if (allTasksCheckbox) {
  allTasksCheckbox.addEventListener('change', function () {
    if (this.checked) {
      priorityCheckboxes.forEach(checkbox => checkbox.checked = false);
    }
  });
}
priorityCheckboxes.forEach(checkbox => {
  checkbox.addEventListener('change', function () {
    if (this.checked) {
      allTasksCheckbox.checked = false;
    }
  });
});

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

  const userId = currentUserId;
  const projectId = activeProjectId;

  const params = new URLSearchParams();
  boardNames.forEach(name => params.append("boardName", name));
  priorities.forEach(priority => params.append("priority", priority));
  params.append("userId", userId);

  fetch(`/board/filter/${projectId}?${params.toString()}`, {
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

