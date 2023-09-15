import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import React from "react";

export type Row ={
    shipName: string
    shipNumber: number
}
export function FleetTable(props: {tableName: string, data: Array<Row>}){
    return(
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 350 }} size="small" aria-label="a dense table">
                <TableHead>
                    <TableRow>
                        <TableCell align="center" colSpan={2}>{props.tableName}</TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell align="center">Ships</TableCell>
                        <TableCell align="center">Remaining</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {props.data.map((row) => (
                        <TableRow
                            key={row.shipName}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell component="th" scope="row">
                                {row.shipName}
                            </TableCell>
                            <TableCell align="right">{row.shipNumber}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    )
}