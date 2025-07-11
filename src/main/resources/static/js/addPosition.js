
    function addInput() {
        let count = $("#jobPositionInputs .job-input").length;
        if (count >= 10) {
            alert("You can't add more than 10 positions");
            return;
        }

        $("#jobPositionInputs").append(
            '<input type="text" class="form-control job-input" name="plusInput" required style="margin-top:5px;">'
        );
    }

    function removeInput() {
        let count = $("#jobPositionInputs .job-input").length;
        if (count > 1) {
            $("#jobPositionInputs .job-input").last().remove();
        }
    }
    $(document).on("submit", "#addJobPositionForm", function(e) {
        e.preventDefault();
        $("input[name='positions']").remove();

        $("#jobPositionInputs .job-input").each(function() {
            $("<input>")
                .attr("type", "hidden")
                .attr("name", "positions")
                .val($(this).val())
                .appendTo("#addJobPositionForm");
        });
        $("#jobPositionInputs .job-input").removeAttr("name");
        this.submit();
        alert("Positions added successfully!");

        $('#addJobPosition').modal('hide');
        $('#addJobPositionForm')[0]?.reset();
    });

