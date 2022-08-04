import {Box} from "grommet";
import React from "react";

export function TextDisplay({textFile}) {
    return (
        <Box background="light-5" fill="horizontal" pad="small">
            {textFile}
        </Box>
    );
}
