import tensorflow as tf
import sys
import numpy as np

#
# input variables for the NN
n_input = 127
n_output = 127
n_hidden1 = 1024

# setup for generating music
# INIT_STATE refers to the starting
# state passed to NN
GENERATING = True
GEN_LENGTH = 120
INIT_STATE = [60, 63, 67]

# threshold for rounding
THRESHOLD = 0.99

def main():
    arglist = sys.argv
    if len(arglist) < 2: # 1D10T error
        print("No input file given.")
        sys.exit()

    # The following code sets up the network as desired,
    # followed by data setup

    x = tf.placeholder(tf.float32, [1, n_input])
    # input matrix of 1x127 vectors (from CSV)
    y = tf.placeholder(tf.float32, [1, n_output])
    # storing 'correct' answer

    W_hidden1 = tf.Variable(tf.zeros([n_input, n_hidden1]))
    b_hidden1 = tf.Variable(tf.zeros([n_hidden1]))
    # weights and biases of hidden layer 1

    out_hidden1 = tf.nn.sigmoid(tf.matmul(x, W_hidden1) + b_hidden1)

    W_out = tf.Variable(tf.zeros([n_hidden1, n_output]))
    b_out = tf.Variable(tf.zeros([n_output]))
    # weights and biases of output layer

    output = tf.nn.sigmoid(tf.matmul(out_hidden1, W_out) + b_out)
    # normalization

    cross_entropy = tf.reduce_mean(
      tf.nn.softmax_cross_entropy_with_logits(labels=y, logits=output))
    # cross_entropy = tf.reduce_mean(-tf.reduce_sum(y * tf.log(output), reduction_indices=[1]))
    # cost function

    train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)
    # optimizer reducing cost function

    init = tf.global_variables_initializer()
    sess = tf.Session()
    sess.run(init)


    # open all files and set up for the run
    file_array = []
    for i in range(1, len(arglist)):
        f = open(str(arglist[i]))
        file_array.append(f)

    # for each midiarray file passed in
    for fp in file_array:
        name = fp.name
        tuple_array = []
        for line in fp:
            tuple_array.append(make_tolerable(line))

        # set up data arrays
        data_array_inorder = []
        for i in tuple_array:
            data_array_inorder.append(i[1])

        # run data through network
        idx = 0
        while (idx < len(data_array_inorder) - 1):
            data1 = np.reshape(data_array_inorder[idx], (1, n_input))
            data2 = np.reshape(data_array_inorder[idx+1], (1, n_output))
            # print(str(data2))
            cost, _,  result = sess.run([cross_entropy, train_step, output],
                            feed_dict={
                                x:data1,
                                y:data2
                            })
            print(name + " iteration " + str(idx+1) + " | cost: " + str(cost))
            # print("Input for this interation: \n" + str(data1))
            # print("Output for this iteration: \n" + str(result))
            idx += 1

    # cleanup for train step
    for fp in file_array:
        fp.close()

    if GENERATING:
        gen_fp = open("tf-output", "w")
        i = 0
        current = generate_state(INIT_STATE)
        # while (i < GEN_LENGTH):
        #     i += 1
        #     current = np.reshape(current, (1, n_input))
        #     compare = current
        #     #print(str(compare))
        #     np.random.shuffle(compare[0])
        #     #print(str(compare))
        #     compare = np.reshape(compare, (1, n_input))
        #     cost, _,  result = sess.run([cross_entropy, train_step, output],
        #                     feed_dict={
        #                         x:current,
        #                         y:compare
        #                     })
        #     print("Generated line " + str(i))
        #     threshold_to_file(gen_fp, result)
        #     result = div_reduce(result[0])
        #     current = result
        while (i < GEN_LENGTH):
            data1 = np.reshape(current, (1, n_input))
            np.random.shuffle(current)
            data2 = np.reshape(current, (1, n_output))
            # print(str(data2))

            # print("DATA 1: \n" + str(data1))
            # print("DATA 2: \n" + str(data2))
            cost, _,  result = sess.run([cross_entropy, train_step, output],
                            feed_dict={
                                x:data1,
                                y:data2
                            })
            # print("RESULT OF D1 and D2: \n" + str(result))
            print(name + " generation line " + str(i+1) + " | cost: " + str(cost))
            # print("Input for this interation: \n" + str(data1))
            # print("Output for this iteration: \n" + str(result))
            #threshold_to_file(gen_fp, result)
            threshold_to_file(gen_fp, result)
            i += 1

    sess.close()
    gen_fp.close()

def div_reduce(arr):
    prod = 0.5
    new_arr = []
    for entry in arr:
        new_arr.append(entry * prod)
    return new_arr


def threshold_to_file(fp, result):
    ret_array = []
    for entry in result[0]:
        # print("ENTRY = \n" + str(entry))
        if (entry < THRESHOLD):
            ret_array.append(0)
        else:
            ret_array.append(1)
    fp.write(str(ret_array) + '\n')

def make_tolerable(line):
    # separate tempo from array
    parts = line.split("[")

    #initialize return val and put tempo in it
    ret_tuple = []
    ret_tuple.append(int(parts[0]))

    # init int-ed ret_array
    ret_array = []
    values = parts[1].split(", ")
    for i in range(0, len(values) - 2):
        ret_array.append(int(values[i]))

    # cast every item in array as int 1 or 0
    for i in range(0, len(ret_array) - 1):
        ret_array[i] = int(ret_array[i])

    ret_tuple.append(ret_array)

    # ret[0] is timimg data
    # ret[1] is inted array
    return ret_tuple

def generate_state(state):
    initial_array = []
    i = 0
    while (i < 127):
        if i in state:
            initial_array.append(1)
        else:
            initial_array.append(0)
        i += 1

    return initial_array


if __name__ == "__main__":
    main()
