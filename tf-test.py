import tensorflow as tf

n_input = 127
n_output = 127
n_hidden1 = 1024

x = tf.placeholder(tf.float32, [None, n_input])
# input matrix of 1x127 vectors (from CSV)
y_ = tf.placeholder(tf.float32, [None, n_output])
# storing 'correct' answer

W_hidden1 = tf.Variable(tf.zeros([n_input, n_hidden1]))
b_hidden1 = tf.Variable(tf.zeros([n_hidden]))
# weights and biases of hidden layer 1

out_hidden1 = tf.nn.sigmoid(tf.matmul(x, W_hidden1) + b)

W_out = tf.Variable(tf.zeros([n_hidden1, n_output]))
b_out = tf.Variable(tf.zeros([n_output]))
# weights and biases of output layer

output = tf.nn.sigmoid(tf.matmul(out_hidden1, W_out) + b_out)
# normalization

cross_entropy = tf.reduce_mean(-tf.reduce_sum(y_ * tf.log(y), reduction_indices=[1]))
# cost function
train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)
# optimizer reducing cost function

init = tf.global_variables_initializer()
sess = tf.Session()
sess.run(init)

## relu, sigmoid
