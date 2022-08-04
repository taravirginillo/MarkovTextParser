import React from "react";
import { Box, PageHeader } from "grommet";
import AAMarkov  from './AAMarkov.jpg'

export const Title = () => {
  return (
    <Box direction="row" justify={"center"}>
      <PageHeader
        title="Markov Chain Parser"
        subtitle="A text parser by Tara Virginillo"
      />
      <img src = {AAMarkov} width={120} height={150}/>
    </Box>
  );
};
