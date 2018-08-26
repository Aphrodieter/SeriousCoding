FOR %%N IN (01, 02, 03, 04, 05, 06, 07, 08, 09, 10) DO (
	FOR %%X IN ("blind" "hadd" "hff" "hmax") DO (
		.\src\pyperplan.py benchmarks/blocks/task%%N.pddl -H %%X -s ehs >> blocks.txt
)			
)