import React from "react";
import { Box, PageHeader } from "grommet";

export const Title = () => {
  return (
    <Box direction="row" justify={"center"}>
      <PageHeader
        title="Markov Chain Parser"
        subtitle="A text parser by Tara Virginillo"
      />
    </Box>
  );
};
