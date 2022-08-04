import React, {Component, useEffect, useState} from "react";

import "./App.css";
import {Box, grommet, Grommet} from "grommet";
import {UserOptions} from "./components/UserOptions";
import {Title} from "./components/Title";
import {TextDisplay} from "./components/TextDisplay";

const AppInnards = () => {
    const [textFile, setTextFile] = useState(null);

    return (
        <Box
            direction="column"
            border={{color: "brand", size: "large"}}
            height={"100vh"}
            pad={"small"}
        >
            <Title/>
            <Box direction="row">
                <UserOptions setTextFile={setTextFile}/>
                <TextDisplay textFile={textFile}/>
            </Box>
        </Box>
    );
};

const App = () => {
    return (
        <Grommet plain theme={grommet}>
            <AppInnards/>
        </Grommet>
    );
};


export default App;
