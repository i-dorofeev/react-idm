import React from 'react';

import { Button, Grid } from 'react-bootstrap';

import PersonTable from './PersonTable';

export default ({personTableData}) => (

    <Grid>
        <Button>New person...</Button>
        <PersonTable data={personTableData} />
    </Grid>
);





