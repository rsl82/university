OUTPUT = ls tree find


%: %.c
	gcc -g -Wall -Wvla -fsanitize=address -o $@ $^


all:$(OUTPUT)
