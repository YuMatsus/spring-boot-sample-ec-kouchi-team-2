// multiselect を Bootstrap 5.x に対応させるための修正スクリプト

$(function(){
    $('.multiselect').multiselect({
        buttonClass: 'form-select',
        templates: {
            button: '<button type="button" class="multiselect dropdown-toggle" data-bs-toggle="dropdown"><span class="multiselect-selected-text"></span></button>'
        }
    });
});