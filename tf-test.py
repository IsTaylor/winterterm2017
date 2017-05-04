import tensorflow as tf
import sys
import numpy as np

n_input = 127
n_output = 127
n_hidden1 = 1024

def main():
    arglist = sys.argv
    if len(arglist) < 2: # 1D10T error
        print("No input file given.")
        sys.exit()

    # The following code sets up the network as desired,
    # followed by data setup

    x = tf.placeholder(tf.float32, [None, n_input])
    # input matrix of 1x127 vectors (from CSV)
    y_ = tf.placeholder(tf.float32, [None, n_output])
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

    cross_entropy = tf.reduce_mean(-tf.reduce_sum(y_ * tf.log(output), reduction_indices=[1]))
    # cost function
    train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)
    # optimizer reducing cost function

    init = tf.global_variables_initializer()
    sess = tf.Session()
    sess.run(init)

    # get data
    fp = open(str(arglist[1]), "r")
    tuple_array = []
    for line in fp:
        tuple_array.append(make_tolerable(line))

    data_array_inorder = []
    for i in tuple_array:
        data_array_inorder.append(i[1])

    # run data
    idx = 0
    while (idx < len(data_array_inorder) - 1):
        cost, output = sess.run([cross_entropy, output],
                        feed_dict={
                            x:data_array_inorder[idx],
                            y_:data_array_inorder[idx+1]
                        })

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



if __name__ == "__main__":
    main()
