import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println();

        // 1. Array List: Kadane's Algorithm [cite: 24]
        int[] array1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("1. Max Subarray Sum: " + maxSubArray(array1));

        // 2. Linked List: Merge Sorted Lists [cite: 25]
        ListNode l1 = new ListNode(1, new ListNode(3, new ListNode(5)));
        ListNode l2 = new ListNode(2, new ListNode(4, new ListNode(6)));
        System.out.print("2. Merged List: ");
        printList(mergeTwoLists(l1, l2));

        // 3. Stack: Min-Stack [cite: 26]
        MinStack stack = new MinStack();
        stack.push(10); stack.push(20); stack.push(5);
        System.out.println("3. Min-Stack: current min = " + stack.getMin());

        // 4. Queue: Sliding Window Maximum [cite: 27]
        int[] array4 = {1, 3, -1, -3, 5, 3, 6, 7};
        System.out.println("4. Window Max (k=3): " + Arrays.toString(maxSlidingWindow(array4, 3)));

        // 5. Hash Table: Group Anagrams [cite: 28]
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println("5. Group Anagrams: " + groupAnagrams(words));

        // 6. BST: K-th Smallest Element [cite: 29]
        TreeNode root = new TreeNode(3, new TreeNode(1, null, new TreeNode(2)), new TreeNode(4));
        System.out.println("6. 2-nd smallest in BST: " + kthSmallest(root, 2));

        // 7. Heap: Min-Heap Implementation [cite: 30, 31]
        MinHeap heap = new MinHeap();
        heap.insert(15); heap.insert(10); heap.insert(20);
        System.out.println("7. Min-Heap: extractMin = " + heap.extractMin());
    }

    // --- TASK 1: Kadane's Algorithm --- [cite: 24]
    public static int maxSubArray(int[] nums) {
        int maxSoFar = nums[0], currentMax = nums[0];
        for (int i = 1; i < nums.length; i++) {
            currentMax = Math.max(nums[i], currentMax + nums[i]);
            maxSoFar = Math.max(maxSoFar, currentMax);
        }
        return maxSoFar;
    }

    // --- TASK 2: Merge Two Sorted Lists --- [cite: 25]
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;   //I used a dummy node to simplify the head of the new list.
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                tail.next = l1; l1 = l1.next;
            } else {
                tail.next = l2; l2 = l2.next;
            }
            tail = tail.next;   //By comparing the heads of both lists, I attached the smaller value to our result and moved the pointers forward.
        }
        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // --- TASK 3: Min-Stack --- [cite: 26]  //This is a standard stack that can retrieve the minimum element in constant time.
    static class MinStack {
        private Stack<Integer> s = new Stack<>(), minS = new Stack<>();
        public void push(int val) {
            s.push(val);                                         //To achieve $O(1)$ speed, I maintained two stacks. 
            if (minS.isEmpty() || val <= minS.peek()) minS.push(val);
        }
        public void pop() {
            if (s.peek().equals(minS.peek())) minS.pop();  
            s.pop();
        }  //
        public int top() { return s.peek(); }
        public int getMin() { return minS.peek(); }
    }  //The primary stack stores all elements, while the auxiliary minStack keeps track of the minimum value at every level of the main stack.

    // --- TASK 4: Sliding Window Maximum --- [cite: 27]  It finds the maximum value in every "window" of size $K$ as it moves across an array.
    public static int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length; //I implemented a Monotonic Queue using a Deque.
        int[] res = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>(); 
        for (int i = 0; i < n; i++) {
            if (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst();
            while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast();
            dq.offerLast(i);
            if (i >= k - 1) res[i - k + 1] = nums[dq.peekFirst()];
        }
        return res;
    }  //This allows me to store indices of elements in a way that the largest element's index is always at the front.

    // --- TASK 5: Group Anagrams --- [cite: 28]  It groups words that are composed of the exact same characters.
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();  //I used a HashMap where the "key" is the sorted version of the word.
        for (String s : strs) {
            char[] ca = s.toCharArray();
            Arrays.sort(ca);
            String key = String.valueOf(ca);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }
//Since all anagrams result in the same string when their characters are sorted alphabetically, they are automatically grouped together in the map.
    // --- TASK 6: K-th Smallest Element in BST --- [cite: 29]  It finds the $K$-th smallest value in a Binary Search Tree.
    private static int count = 0, result = -1;
    public static int kthSmallest(TreeNode root, int k) {
        count = 0; //I utilized the fundamental property of BSTs: an In-order traversal visits nodes in strictly increasing order.
        traverse(root, k);
        return result;
    }
    private static void traverse(TreeNode node, int k) {
        if (node == null) return;
        traverse(node.left, k);
        if (++count == k) { result = node.val; return; }
        traverse(node.right, k);
    }  //I simply perform this traversal and stop at the $K$-th element. 

    // --- TASK 7: Min-Heap from Scratch --- [cite: 30, 31]  A "from-scratch" implementation of a binary min-heap.
    static class MinHeap {
        private List<Integer> heap = new ArrayList<>();
        public void insert(int val) {
            heap.add(val);
            int cur = heap.size() - 1;  //The heap is represented as an ArrayList.
            while (cur > 0 && heap.get(cur) < heap.get((cur - 1) / 2)) {
                Collections.swap(heap, cur, (cur - 1) / 2);
                cur = (cur - 1) / 2;
            }
        }
        public int extractMin() {
            int min = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) { heap.set(0, last); heapify(0); }
            return min;
        }
        public void heapify(int i) {
            int l = 2 * i + 1, r = 2 * i + 2, smallest = i;
            if (l < heap.size() && heap.get(l) < heap.get(smallest)) smallest = l;
            if (r < heap.size() && heap.get(r) < heap.get(smallest)) smallest = r;
            if (smallest != i) { Collections.swap(heap, i, smallest); heapify(smallest); }
        }
    } //I implemented siftUp for insertions to maintain the heap property and a heapify  method for the extractMin operation.

    // Вспомогательные классы и методы
    static class ListNode {
        int val; ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    private static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + (head.next != null ? " -> " : ""));
            head = head.next;
        }
        System.out.println();
    }
}
