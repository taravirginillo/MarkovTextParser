import {Button} from "grommet";

export const SubmitButton = ({isSubmitButtonEnabled}) => {
    return <Button primary type="submit" label="Submit" disabled = {!isSubmitButtonEnabled}/>;
};