# Finding-consistency-in-an-inconsistent-data
To detect data consistency ‘Detection algorithm’ will run with following steps
1)	First we will scan database and pass result to Detection Algorithm as input
2)	Group & Range function will call to group all query result based on same column values.
3)	LHS and RHS rule will be applied for subset matching
4)	If subset of right match with left side records then no data inconsistency occur and query output will be merged.
5)	Sort all records and then group all records in output based on Denial Constraints such as =,>,<, > =,<=,!= etc.
