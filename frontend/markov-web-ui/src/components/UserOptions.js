import React, {useState} from "react";
import {Box, FileInput, Form, FormField, TextInput} from "grommet";
import {SubmitButton} from "./SubmitButton";
import {ResetButton} from "./ResetButton";
import {handleSubmit} from "../utils/HandleSubmit";

export function UserOptions({setTextFile}) {
    // const [prefixSize, setPrefixSize] = useState(1);
    // const [maxOutputSize, setMaxOutputSize] = useState(10);
    // const [rawFile, setRawFile] = useState(null);
    // const [isRequestLoading, setIsRequestLoading] = useState(false);

    const defaultValue = {prefixSize: 1, maxOutputSize: 10}

    const [value, setValue] = useState(defaultValue);
    const isSubmitButtonEnabled = value.file != null;

    return (
        <Box fill={"horizontal"} pad="small">
            <Form
                value={value}
                onChange={nextValue => setValue(nextValue)}
                onReset={() => setValue(defaultValue)}
                onSubmit={({value}) => {
                    return handleSubmit({
                        prefixSize: value.prefixSize,
                        maxOutputSize: value.maxOutputSize
                    }, value.file, setTextFile)
                }}
            >
                <FormField name="prefixSize" label="Prefix Size">
                    <TextInput type="number" name="prefixSize"/>
                </FormField>


                <FormField name="maxOutputSize" label="Max Output Size">
                    <TextInput type="number" name="maxOutputSize"/>
                </FormField>


                <FormField name="file" label="File Upload">
                    <FileInput name="file"/>
                </FormField>

                <Box direction="row" justify={"around"}>
                    <SubmitButton isSubmitButtonEnabled={isSubmitButtonEnabled}/>
                    <ResetButton/>
                </Box>

            </Form>
        </Box>
    );
}

