import React from 'react'
import axios from 'axios'

export default function Text() {
    const submitText = (event) => {
        axios
        .get("http://localhost:9000/", {
            headers: {
            "Content-Type": "application/json"
            }
        })
        .then((response) => {
            // successfully uploaded response
        })
        .catch((error) => {
            // error response
        });
    };
  return (
    <button onChange={(e) => submitText(e)}>Parse Text</button>
  )
}
