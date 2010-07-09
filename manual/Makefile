all: manual.html manual.pdf

manual.html: manual.md
	mmd2web.pl manual.md

manual.tex: manual.md
	mmd2LaTeX.pl manual.md

manual.pdf: manual.tex
	pdflatex manual.tex

manual.md: *.md
	mmd_merge.pl index > manual.md
