import axios from 'axios'

export const handleSubmit = async (parameters, file, setRawText) => {
    const requestURI = buildRequestURI("text", parameters);

    let formData = new FormData();
    formData.append('file', file[0]);
    axios.post(
        requestURI,formData
    ).then(function (response) {
        console.log(response);
        setRawText(response.data);
     }).catch(
     function(error) { 
        console.log(error);
        setRawText(error.message);
     });
};

const buildRequestURI = (base, parameters) => {
    const searchParams = new URLSearchParams(parameters);
    return `/${base}?${searchParams}`;
};
