import axios from 'axios'

export const handleSubmit = async (parameters, file, setRawText) => {
    const requestURI = buildRequestURI("text", parameters);
    const requestHeaders = getRequestHeaders(file);

    let formData = new FormData();
    formData.append('file', file[0]);
    console.log(requestURI);
    console.log(formData);
    fetch(
        requestURI,
        {
            method: 'POST',
            formData,
            headers:{
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (response) {
        //handle success
        console.log(response);
        return response.blob();
     }, 
     function(error) { 
        console.log(error);
        return(error.response);
        // handle error 
     });
};

const buildRequestURI = (base, parameters) => {
    const searchParams = new URLSearchParams(parameters);
    return `/${base}?${searchParams}`;
};

const getRequestHeaders = (file) => {
    console.log(file);
    const formData = new FormData();
    formData.append('file', file);
    return {
        method: "POST",
        headers: {
            "Content-Type": "multipart/form-data",
        },
        formData
    };
};
