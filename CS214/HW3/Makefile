OUTPUT = shell


%: %.c
	gcc -g -Wall -Wvla -fsanitize=address -o $@ $^


all:$(OUTPUT)
