all: manual.html manual.pdf

manual.html: manual.md
	mmd2web.pl manual.md

manual.pdf: manual.md
	mmd2PDF.pl manual.md

manual.md: *.md
	mmd_merge.pl index > manual.md
