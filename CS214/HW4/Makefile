
CC = gcc
CFLAGS = -fsanitize=address -g -Wall -Wvla
DEPS = mymalloc.h
OBJS = memperf.o mymalloc.o



%.o: %.c $(DEPS)
	$(CC) $(CFLAGS) -c -o $@ $<

memperf: $(OBJS)
	$(CC) $(CFLAGS) -o $@  $^